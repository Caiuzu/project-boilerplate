import React from 'react';
import { FormItemProps } from 'antd/lib/form/FormItem';

export interface FloatInputProps extends FormItemProps {
	label?: string;
	children: React.JSX.Element;
}
