'use client';

import React from 'react';
import { Col, Row, Card, Form, Alert, Input, Button } from 'antd';

import styles from './styles.module.scss';
import FloatItem from '~/components/commons/form/FloatItem';
import { userService } from '~/app/usecase/service/user.service';

interface Errors {
	message: string;
}

const ForgotPassword = () => {
	const [errors, setErrors] = React.useState<Errors>();
	const [message, setMessage] = React.useState('');
	const [isLoading, setIsLoading] = React.useState(false);

	const onSubmit = ({ email }: { email: string }) => {
		setIsLoading(true);
		return userService
			.forgot(email)
			.then((res) => {
				setIsLoading(false);
				setMessage('Email enviado');
			})
			.catch((error) => {
				setIsLoading(false);
				setErrors({ message: 'Ocorreu um erro ao enviar o email' });
				console.error(error);
			});
	};

	const validateMessages = {
		types: {
			email: '${label} não é um email válido!'
		}
	};

	const onCloseError = () => {
		setTimeout(() => {
			setErrors(undefined);
		}, 300);
	};

	return (
		<div className={styles.container}>
			<Card className={styles.cardContainer} style={{ padding: 0, width: '28rem' }}>
				<Row style={{ padding: '40px' }} className={styles.cardContent}>
					<Col span={24}>
						<h2 style={{ marginBottom: '32px' }}>Esqueceu sua senha?</h2>
					</Col>
					<Form onFinish={onSubmit} style={{ width: '100%' }} validateMessages={validateMessages}>
						<Row gutter={16}>
							<Col span={24}>
								<FloatItem name="email" label="Email" rules={[{ type: 'email', required: true }]}>
									<Input />
								</FloatItem>
							</Col>
							<Col span={24}>
								<Button type="primary" htmlType="submit" loading={isLoading} style={{ width: '100%' }}>
									Enviar
								</Button>
							</Col>
						</Row>
					</Form>
					{errors?.message && (
						<Alert closable type="error" onClose={onCloseError} message={errors?.message} style={{ width: '100%', marginTop: '16px' }} />
					)}
					{message && <Alert closable type="success" message={message} style={{ width: '100%', marginTop: '16px' }} />}
				</Row>
			</Card>
		</div>
	);
};

export default ForgotPassword;
