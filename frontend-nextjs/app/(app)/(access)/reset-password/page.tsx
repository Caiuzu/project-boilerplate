import type { Metadata } from 'next';

import React from 'react';

import { Props } from '~/app/(app)/types';
import ResetPassword from '~/components/Login/ResetPassword';

export const metadata: Metadata = {
	title: 'Trocar senha'
};

const ResetPasswordPage = async (props: Props) => {
	const searchParams = await props.searchParams;
	const { token } = searchParams;
	return <ResetPassword token={token as string} />;
};

export default ResetPasswordPage;
