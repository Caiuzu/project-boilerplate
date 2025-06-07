import React from 'react';
import { Form } from 'antd';

import FormField from './FormField';
import styles from './styles.module.scss';
import { FloatInputProps } from './types';

const FloatItem: React.FC<FloatInputProps> = ({ children, ...props }) => {
	const { name, label, rules, ...rest } = props;
	const { hasFeedback = false, validateStatus = '', ...otherProps } = rest;

	return (
		<Form.Item name={name} rules={rules} hasFeedback={hasFeedback} className={styles.formItem} validateStatus={validateStatus}>
			<FormField name={name} label={label} {...otherProps}>
				{children}
			</FormField>
		</Form.Item>
	);
};

export default FloatItem;
