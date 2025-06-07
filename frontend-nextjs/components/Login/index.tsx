'use client';

import React from 'react';
import { Col, Row, Card, Typography } from 'antd';

import LoginForm from './Form';
import styles from './styles.module.scss';
import { useRouter } from '~/app/usecase/hooks/useRouter';
import { useSession } from '~/app/usecase/contexts/AuthContext';

const { Title } = Typography;

const Login = () => {
	const router = useRouter();
	const { session } = useSession();

	React.useEffect(() => {
		if (session.token) {
			router.push('/board');
		}
	}, []);

	return (
		<div className={styles.container}>
			<Card className={styles.cardContainer} style={{ padding: 0, width: '28rem' }}>
				<Row className={styles.cardContent}>
					<Col span={24}>
						<LoginForm />
					</Col>
				</Row>
			</Card>
		</div>
	);
};

export default Login;
