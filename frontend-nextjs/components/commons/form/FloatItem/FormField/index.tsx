import type { Dayjs } from 'dayjs';

import React from 'react';

import styles from './styles.module.scss';
import { FieldProps, FormFieldProps } from './types';
import { isNotEmpty } from '~/app/usecase/util/stringUtils';

const Field: React.FC<FieldProps> = ({ children, setValue, onChange }) => {
	const fieldOnChange = onChange;
	const initialOnChange = children?.props?.onChange;
	const handleChange = (props: Dayjs | string | number | React.ChangeEvent<HTMLInputElement>, targetValue: string | string[]) => {
		let value;

		const isString = typeof props === 'string';
		const isNumber = typeof props === 'number';
		const isChangeEvent = !isString && !isNumber && props && 'target' in props;

		if (isString) {
			value = props;
		} else if (isNumber) {
			value = props.toString();
		} else if (isChangeEvent) {
			value = props.target.value;
		} else {
			value = targetValue;
		}

		const isInput = children?.type && 'TextArea' in children.type;
		if (typeof fieldOnChange === 'function') {
			if (isInput) {
				fieldOnChange(props);
			} else {
				fieldOnChange(value);
			}
		}
		typeof initialOnChange === 'function' && initialOnChange(props, targetValue);

		setValue(value);
	};

	const extraProp = {
		onChange: handleChange
	};

	return React.cloneElement(children, extraProp);
};

const FormField: React.FC<FormFieldProps> = (props) => {
	const { name, label, style, children, ...otherProps } = props;
	const [focus, setFocus] = React.useState(false);
	const [value, setValue] = React.useState(children?.props.value);

	const hasPrefix = children?.props?.prefix;
	const hasValue = isNotEmpty(value) || isNotEmpty(otherProps?.value);
	const isOccupied = focus || hasValue;
	const labelClass = isOccupied ? `${styles.label} ${styles.asLabel}` : `${styles.label} ${styles.asPlaceholder}`;
	const requiredMark = props['aria-required'] ? <span className={styles.textDanger}>*</span> : null;
	const withPrefix = hasPrefix ? `${styles.withPrefix}` : '';

	const isTag = children?.props?.mode === 'tags';

	const extraProp = {
		name,
		...otherProps,
		placeholder: null,
		value: children?.props?.value || otherProps?.value
	};

	React.useEffect(() => {
		setValue(children?.props.value);
	}, [children?.props.value]);

	const tagClass = isTag ? `${styles.tag}` : '';
	const onChangeFunction = children?.props?.onChange;

	return (
		<div style={style} onBlur={() => setFocus(false)} onFocus={() => setFocus(true)} className={`${styles.floatLabel} ${tagClass}`}>
			{React.Children.map(children, (child) => (
				<Field setValue={setValue} onChange={onChangeFunction}>
					{React.cloneElement(child!, extraProp)}
				</Field>
			))}
			{label && (
				<label htmlFor={name} className={`${labelClass} ${withPrefix}`}>
					{label} {requiredMark}
				</label>
			)}
		</div>
	);
};

export default FormField;
