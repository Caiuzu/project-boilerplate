'use client';

import React from 'react';
import { useRouter } from '~/app/usecase/hooks/useRouter';

const Home = () => {
	const router = useRouter();
	const [redirected, setRedirected] = React.useState(false);

	React.useEffect(() => {
		router.push('/board/portal');
		setRedirected(true);
	}, [router]);

	if (!redirected) {
		return (
			<main style={{ padding: 32, textAlign: 'center' }}>
				<h1>Bem-vindo ao Frontend Boilerplate Next.js!</h1>
				<p>Esta é a página inicial padrão. Personalize conforme seu projeto.</p>
			</main>
		);
	}
	return null;
};

export default Home;
