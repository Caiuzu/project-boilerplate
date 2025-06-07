'use client';

import React from 'react';
import { setCookie } from 'cookies-next';
import { Col, Form, Alert, Input, Button, Typography } from 'antd';

import { Error } from './types';
import styles from './styles.module.scss';
import { useRouter } from '~/app/usecase/hooks/useRouter';
import FloatItem from '~/components/commons/form/FloatItem';
import { SESSION_COOKIE } from '~/app/usecase/util/Constants';
import { userService } from '~/app/usecase/service/user.service';
import { passRules, emailRules, initialValues } from './constants';
import { LoginRequest } from '~/app/domain/interface/user.interface';

const { Link } = Typography;

const LoginForm = () => {
	const router = useRouter();

	const [loading, setLoading] = React.useState(false);
	const [errors, setErrors] = React.useState<null | Error>(null);

	const onSubmit = (values: LoginRequest) => {
		setLoading(true);
		userService
			.login(values)
			.then((user) => {
				if (user) {
					setCookie(SESSION_COOKIE, JSON.stringify(user));
					router.push('/board');
				}
			})
			.catch((error) => {
				setErrors({ message: 'Usuário ou senha inválidos' });
				setLoading(false);
			});
	};

	return (
		<div className={styles.loginForm}>
			<Form onFinish={onSubmit} style={{ width: '100%' }} initialValues={initialValues}>
				<Col xs={24}>
					<FloatItem label="Login" name="username" rules={emailRules}>
						<Input />
					</FloatItem>
				</Col>
				<Col xs={24}>
					<FloatItem name="password" label="Password" rules={passRules}>
						<Input.Password />
					</FloatItem>
				</Col>
				<Col xs={24} className={styles.forgotPassword}>
					<Link href="/forgot-password">Esqueceu sua senha?</Link>
				</Col>
				<Col sm={24}>
					<Button type="primary" htmlType="submit" loading={loading} style={{ width: '100%' }}>
						Entrar
					</Button>
				</Col>
			</Form>
			{errors && (
				<Col xs={24} style={{ marginTop: 16 }}>
					<Alert closable type="error" message={errors?.message} />
				</Col>
			)}
		</div>
	);
};

export default LoginForm;
