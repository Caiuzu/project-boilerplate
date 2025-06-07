'use client';

import React from 'react';
import { Layout } from 'antd';
import { Content } from 'antd/lib/layout/layout';

const EmptyLayout = ({ children }: { children: React.ReactNode }) => {
	return (
		<Layout style={{ width: '100%', minHeight: '100vh' }}>
			<Content style={{ padding: 0 }}>{children}</Content>
		</Layout>
	);
};

export default EmptyLayout;
