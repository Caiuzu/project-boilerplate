'use client';

import React from 'react';
import { ConfigProvider } from 'antd';
import { Inter } from 'next/font/google';
import { AntdRegistry } from '@ant-design/nextjs-registry';

import { theme } from '~/styles/theme';
import { AuthProvider } from './AuthContext';
import { MessageProvider } from './MessageContext';
import { showInsetEffect } from '~/app/usecase/util/showEffect';

const inter = Inter({ subsets: ['latin'] });

export const Providers = ({ appAuth, children }: React.PropsWithChildren<{ appAuth: any | null }>) => {
	const themeConfig = {
		...theme,
		token: {
			...theme.token,
			fontFamily: inter.style.fontFamily
		}
	};

	return (
		<ConfigProvider theme={themeConfig} wave={{ showEffect: showInsetEffect }}>
			<AntdRegistry>
				<AuthProvider appAuth={appAuth}>
					<MessageProvider>{children}</MessageProvider>
				</AuthProvider>
			</AntdRegistry>
		</ConfigProvider>
	);
};
