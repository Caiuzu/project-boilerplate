import type { Metadata } from 'next';

import React from 'react';
import { Inter } from 'next/font/google';
import '@ant-design/v5-patch-for-react-19';
import { getMessages } from 'next-intl/server';
import { NextIntlClientProvider } from 'next-intl';

import '~/styles/globals.scss';
import { Providers } from '~/app/usecase/contexts';
import AppLayout from '~/components/commons/Layout';
import { boardService } from '../usecase/service/board.service';
import { getUser } from '~/app/usecase/contexts/AuthContext/utils';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
	alternates: {
		canonical: './'
	},
	title: {
		default: 'KIT',
		template: '%s - Kit - Lorem Ipsum'
	}
};

const RootLayout = async (props: { children: React.ReactNode; params: Promise<{ locale: string }> }) => {
	const params = await props.params;

	const { locale } = params;
	const { children } = props;
	const messages = await getMessages();

	const auth = await getUser();
	const boardMenu = await boardService.getBoardMenu(auth?.token || '');

	return (
		<html lang={locale}>
			<body className={inter.className}>
				<Providers appAuth={auth}>
					<NextIntlClientProvider locale={locale} messages={messages}>
						<AppLayout menu={boardMenu}>{children}</AppLayout>
					</NextIntlClientProvider>
				</Providers>
			</body>
		</html>
	);
};

export default RootLayout;
