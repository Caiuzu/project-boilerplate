import type { Metadata } from 'next';

import React from 'react';

import ForgotPassword from '~/components/Login/ForgotPassword';

export const metadata: Metadata = {
	title: 'Recuperar senha'
};

const ForgotPasswordPage = () => {
	return <ForgotPassword />;
};

export default ForgotPasswordPage;
