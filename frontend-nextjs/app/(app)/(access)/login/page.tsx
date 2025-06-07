import type { Metadata } from 'next';

import React from 'react';

import Login from '~/components/Login';

export const metadata: Metadata = {
	title: 'Login'
};

const LoginPage = () => {
	return <Login />;
};

export default LoginPage;
