import type { Dayjs } from 'dayjs';

import React from 'react';

export interface FormFieldProps {
	name?: string;
	label?: string;
	value?: string;
	placeholder?: string;
	'aria-required'?: boolean;
	style?: React.CSSProperties;
	children?: React.JSX.Element;
}

export type FieldOnChangeFunction = (value: Dayjs | string | number | string[] | React.ChangeEvent<HTMLInputElement>) => void;

export interface FieldProps {
	children: React.JSX.Element;
	onChange: FieldOnChangeFunction;
	setValue: React.Dispatch<React.SetStateAction<string | string[]>>;
}
