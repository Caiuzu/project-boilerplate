import React from 'react';

import '~/styles/globals.scss';
import EmptyLayout from '~/components/commons/Layout/EmptyLayout';

const RootLayout = async (props: { children: React.ReactNode }) => {
	const { children } = props;
	const isProduction = process.env.APP_ENV === 'production';
	return <EmptyLayout>{children}</EmptyLayout>;
};

export default RootLayout;
