'use client';

import React from 'react';
import { Typography } from 'antd';
import { MessageOutlined } from '@ant-design/icons';

import { HomePageProps } from './types';
import styles from './styles.module.scss';
import SubBoard from '~/components/Board/SubBoard';
import Breadcrumbs from '~/components/Board/Breadcrumbs';
import LatestTopics from '~/components/Board/LatestTopics';

const { Title } = Typography;

const HomePage = ({ data, latestTopics }: HomePageProps) => {
	return (
		<div className={styles.boardContainer}>
			<Title hidden>Kit</Title>
			<div className={styles.board}>
				<Breadcrumbs items={[]} />
				<LatestTopics latestTopics={latestTopics} />
				{data.map((board, index) => (
					<div key={index} className={styles.boardCategory}>
						<Title level={2} style={{ display: 'flex', alignItems: 'center' }}>
							<MessageOutlined style={{ marginRight: '8px' }} /> {board.title}
						</Title>
						{board.children && board.children.map((subBoards, subIndex) => <SubBoard key={subIndex} subBoards={subBoards} />)}
					</div>
				))}
			</div>
			<div className={styles.sidebar}>
				<iframe width="330" height="400" style={{ border: 'none' }} src="https://discordapp.com/widget?id=000000000000000000&theme=dark" />
			</div>
		</div>
	);
};

export default HomePage;
