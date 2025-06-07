'use client';

import React from 'react';
import Link from 'next/link';
import Image from 'next/image';
import { usePathname } from 'next/navigation';
import { HomeFilled, MenuOutlined } from '@ant-design/icons';
import { Footer, Header, Content } from 'antd/lib/layout/layout';
import { Col, Row, Menu, Avatar, Button, Layout, Dropdown, MenuProps } from 'antd';

import styles from './styles.module.scss';
import { useSession } from '~/app/usecase/contexts/AuthContext';
import { useScrollPosition } from '~/app/usecase/hooks/useScrollPosition';
import { BoardMenuResponse } from '~/app/domain/interface/board.interface';
import { getProfileLink, getUserImageUrl } from '~/app/usecase/util/stringUtils';

const AppLayout = ({ menu, children }: { children: React.ReactNode; menu: BoardMenuResponse[] }) => {
	const { session } = useSession();
	const pathname = usePathname();

	const items: MenuProps['items'] = [
		{
			key: 'profile',
			label: <Link href={getProfileLink(session?.user?.id, session?.user?.name)}>Profile</Link>
		},
		{
			key: 'logout',
			label: <Link href={'/logout'}>Logout</Link>
		}
	];

	const menuLink = (item: BoardMenuResponse) => {
		if (item.app.toLowerCase() === 'boards') return `/board`;
		if (item.app.toLowerCase() === 'portal') return `/board/portal`;
		const itemConfigJson = JSON.parse(item.config);
		if (Array.isArray(itemConfigJson)) return null;
		if (itemConfigJson && typeof itemConfigJson === 'object') {
			const boardId = itemConfigJson.menu_custom_item_url
				?.split('&')
				.find((param: string) => param.startsWith('id='))
				?.split('=')[1];

			const seoTitle = itemConfigJson?.seoTitles?.[0];
			const prefix = itemConfigJson?.internal && itemConfigJson?.internal === 'boards_board' ? '/board/board' : 'portal/';

			return `${prefix}${boardId ? `/${boardId}-${seoTitle}` : `/${seoTitle}`}`;
		}
		return null;
	};

	const menuItems = menu.map((item) => {
		const isBoard = item.app.toLowerCase() === 'boards';
		const icon = item.app.toLowerCase() === 'boards' ? <HomeFilled /> : <MenuOutlined />;
		const link = menuLink(item);
		const title = item.title ? item.title : item.extension;
		return {
			icon: icon,
			key: item.id,
			label: link ? <Link href={link}>{title}</Link> : title,
			...(item.children.length > 0 &&
				!isBoard && {
					children: item.children.map((child) => {
						const link = menuLink(child);
						const title = child.title ? child.title : child.extension;
						return {
							key: child.id,
							label: link ? <Link href={link}>{title}</Link> : title
						};
					})
				})
		};
	});

	const getSelectedKey = () => {
		if (pathname === '/board') return 'board';
		if (pathname.startsWith('/board/portal')) return 'portal';
		return '';
	};

	useScrollPosition();

	return (
		<Layout className={styles.publicLayout} style={{ width: '100%', minHeight: '100vh' }}>
			<Header className={`${styles.header}`} style={{ padding: 0, background: '#272727' }}>
				<Row className={styles.headerContent}>
					<Col span={22} className={styles.headerMenu}>
						<Link href={'/board'}>
							<div className={styles.logo}>
								<Image alt="Logo" width={300} height={64} src="/img/logo.png" />
							</div>
						</Link>
					</Col>
					<Col span={2} className={styles.headerUser}>
						{session.token ? (
							<Dropdown menu={{ items }} trigger={['click']}>
								<div style={{ display: 'flex', cursor: 'pointer', alignItems: 'center' }}>
									<Avatar src={getUserImageUrl(session.user.avatar)} />
									<span style={{ marginLeft: 8, color: '#ffffffd8' }}>{session.user.name}</span>
								</div>
							</Dropdown>
						) : (
							<Link href="/login">
								<Button>Login</Button>
							</Link>
						)}
					</Col>
				</Row>
				<Row className={styles.homeMenu}>
					<Col span={24} className={styles.headerContent}>
						<Menu theme="dark" mode="horizontal" items={menuItems} rootClassName={styles.rootMenu} selectedKeys={[getSelectedKey()]} />
					</Col>
				</Row>
			</Header>
			<Content style={{ padding: 0 }}>{children}</Content>
			<Footer className={styles.footer}>
				<div className={styles.footerContent}>
					<span>© {new Date().getFullYear()} Kit. All rights reserved.</span>
					<Link href="/privacy">Política De Privacidade</Link>
				</div>
			</Footer>
		</Layout>
	);
};

export default AppLayout;
