import IMask from 'imask';
import React from 'react';
import { InputRef } from 'antd';
import Input, { InputProps } from 'antd/lib/input';

export interface MaskedInputProps extends Omit<InputProps, 'value' | 'onChange' | 'defaultValue'> {
	mask: MaskType;
	value?: string;
	defaultValue?: string;
	maskOptions?: InputMaskOptions;
	onChange?: (event: string) => any;
	definitions?: InputMaskOptions['definitions'];
}

export { IMask };

const KEY_PRESS_EVENT = keyPressPropName();

export const MaskedInput = React.forwardRef<InputRef, MaskedInputProps>(function MaskedInput(props: MaskedInputProps, antdRef) {
	const { mask, onChange, definitions, defaultValue, value: _value, maskOptions: _maskOptions, ...antdProps } = props;

	const innerRef = React.useRef<null | HTMLInputElement>(null);

	const maskOptions = React.useMemo(() => {
		return {
			mask,
			definitions: {
				'0': /[0-9]/,
				..._maskOptions?.definitions,
				...definitions
			},
			..._maskOptions
		} as IMaskOptions;
	}, [_maskOptions, definitions, mask]);

	// @ts-ignore
	const imask = React.useRef<null | IMask.InputMask<any>>(null);

	const propValue = (typeof _value === 'string' ? _value : defaultValue) || '';

	const lastValue = React.useRef(propValue);

	const [value, setValue] = React.useState(propValue);

	const _onEvent = React.useCallback(
		(ev: any, callOnChangeProps = false) => {
			const masked = imask.current;
			if (!masked) return;

			if (ev.target) {
				if (ev.target.value !== masked.value) {
					masked.value = ev.target.value;
					ev.target.value = masked.value;
					lastValue.current = masked.value;
				}
			}

			Object.assign(ev, {
				maskedValue: masked.value,
				unmaskedValue: masked.unmaskedValue
			});

			masked.updateValue();
			setValue(lastValue.current);

			if (callOnChangeProps) {
				onChange?.(ev);
			}
		},
		[onChange]
	);

	const _onAccept = React.useCallback(
		(ev: any) => {
			if (!ev?.target) return;

			const input = innerRef.current;
			const masked = imask.current;
			if (!input || !masked) return;

			ev.target.value = masked.value;
			input.value = masked.value;
			lastValue.current = masked.value;

			_onEvent(ev, true);
		},
		[_onEvent]
	);

	const updateMaskRef = React.useCallback(() => {
		const input = innerRef.current;

		if (imask.current) {
			imask.current.updateOptions(maskOptions as any);
		}

		if (!imask.current && input) {
			imask.current = IMask<any>(input, maskOptions);
			imask.current.on('accept', _onAccept);
		}

		if (imask.current && imask.current.value !== lastValue.current) {
			imask.current.value = lastValue.current;
			imask.current.alignCursor();
		}
	}, [_onAccept, maskOptions]);

	function updateValue(value: string) {
		lastValue.current = value;
		const input = innerRef.current;
		const masked = imask.current;
		if (!(input && masked)) return;
		masked.value = value;
		// updating value with the masked
		//   version (imask.value has useClientMediaQuery.ts setter that triggers masking)
		input.value = masked.value;
		lastValue.current = masked.value;
	}

	React.useEffect(() => {
		updateMaskRef();

		return () => {
			imask.current?.destroy();
			imask.current = null;
		};
	}, [mask, updateMaskRef]);

	React.useEffect(() => {
		updateValue(propValue);
	}, [propValue]);

	const eventHandlers = React.useMemo(() => {
		return {
			onBlur(ev: any) {
				_onEvent(ev);
				props.onBlur?.(ev);
			},

			onFocus(ev: any) {
				_onEvent(ev);
				props.onFocus?.(ev);
			},

			[KEY_PRESS_EVENT]: (ev: any) => {
				_onEvent(ev, true);
				props[KEY_PRESS_EVENT]?.(ev);
			},

			onPaste(ev: React.ClipboardEvent<HTMLInputElement>) {
				lastValue.current = ev.clipboardData?.getData('text');

				if (ev.target) {
					// @ts-ignore
					ev.target.value = lastValue.current;
				}

				_onEvent(ev, true);
				props.onPaste?.(ev);
			}
		};
	}, [_onEvent, props]);

	return (
		<Input
			{...antdProps}
			{...eventHandlers}
			value={value}
			onChange={(ev) => _onEvent(ev, true)}
			ref={function handleInputMask(ref) {
				if (antdRef) {
					if (typeof antdRef === 'function') {
						antdRef(ref);
					} else {
						antdRef.current = ref;
					}
				}

				if (ref?.input) {
					innerRef.current = ref.input;
					if (!imask.current) {
						updateMaskRef();
					}
				}
			}}
		/>
	);
});

function keyPressPropName() {
	if (typeof navigator !== 'undefined') {
		return navigator.userAgent.match(/Android/i) ? 'onBeforeInput' : 'onKeyPress';
	}
	return 'onKeyPress';
}

export default MaskedInput;

interface IMaskOptionsBase {
	definitions?: Record<string, RegExp>;
}

export type InputMaskOptions = {
	[K in keyof IMaskOptionsBase]?: IMaskOptionsBase[K];
};

type MaskFieldType = Date | string | RegExp | Function | InputMaskOptions;

interface IMaskOptions extends Omit<InputMaskOptions, 'mask'> {
	mask: MaskFieldType;
}

interface MaskOptionsList extends Array<IMaskOptions> {}

export type MaskType = MaskFieldType | MaskOptionsList;
