'use client';

import React from 'react';
import { Col, Row, Card, Form, Alert, Input, Button } from 'antd';

import styles from './styles.module.scss';
import { useRouter } from '~/app/usecase/hooks/useRouter';
import FloatItem from '~/components/commons/form/FloatItem';
import { userService } from '~/app/usecase/service/user.service';

interface Errors {
	message: string;
}

const ResetPassword = ({ token }: { token: string }) => {
	const router = useRouter();
	const [errors, setErrors] = React.useState<Errors>();
	const [message, setMessage] = React.useState('');
	const [isLoading, setIsLoading] = React.useState(false);

	const onSubmit = ({ password }: { password: string }) => {
		setIsLoading(true);
		userService
			.reset(token, password)
			.then(() => {
				router.push('/login');
			})
			.catch((error) => {
				setErrors({ message: error });
			});
	};

	const initialValues = {
		token: token,
		password: '',
		confirmPassword: ''
	};

	const onCloseError = () => {
		setTimeout(() => {
			setErrors(undefined);
		}, 300);
	};

	const passwordMatchValidator = ({ getFieldValue }: { getFieldValue: (field: string) => any }) => ({
		validator(_: any, value: any) {
			if (!value || getFieldValue('password') === value) {
				return Promise.resolve();
			}
			return Promise.reject(new Error('The two passwords do not match!'));
		}
	});

	return (
		<div className={styles.container}>
			<Card className={styles.cardContainer} style={{ padding: 0, width: '28rem' }}>
				<Row style={{ padding: '40px' }} className={styles.cardContent}>
					<Col span={24}>
						<h2 style={{ marginBottom: '32px' }}>Troque sua senha</h2>
					</Col>
					<Form onFinish={onSubmit} style={{ width: '100%' }} initialValues={initialValues}>
						<Row gutter={16}>
							<Col span={24}>
								<FloatItem label="Senha" name="password" rules={[{ required: true }]}>
									<Input.Password />
								</FloatItem>
							</Col>
							<Col span={24}>
								<FloatItem name="confirmPassword" label="Confirmar senha" rules={[{ required: true }, passwordMatchValidator]}>
									<Input.Password />
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

export default ResetPassword;
