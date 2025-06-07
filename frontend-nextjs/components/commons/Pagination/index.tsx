'use client';

import React from 'react';
import { usePathname } from 'next/navigation';
import { Button, Pagination as AntPagination } from 'antd';
import { DoubleLeftOutlined, DoubleRightOutlined } from '@ant-design/icons';

import style from './style.module.scss';
import { PAGE_SIZE } from '~/app/usecase/util/Constants';
import { useRouter } from '~/app/usecase/hooks/useRouter';

interface PaginationProps {
	count: number;
	page?: string | number;
	setLoading?: (loading: boolean) => void;
}

const Pagination: React.FC<PaginationProps> = ({ count, page = 0, setLoading }) => {
	const router = useRouter();
	const pathname = usePathname();

	if (count <= PAGE_SIZE) {
		return null;
	}

	const handlePageChange = (page: number) => {
		setLoading && setLoading(true);
		const basePath = pathname.replace(/\/page\/\d+$/, '');
		router.push(`${basePath}/page/${page}`);
	};

	const itemRender = (
		current: number,
		type: 'page' | 'prev' | 'next' | 'jump-prev' | 'jump-next',
		originalElement: React.ReactNode
	): React.ReactNode => {
		if (type === 'prev') {
			return current === 0 ? null : <Button className={style.prev}>Anterior</Button>;
		}
		if (type === 'next') {
			const totalPages = Math.ceil(count / PAGE_SIZE);
			return Number(page) === totalPages ? null : <Button className={style.next}>Próxima</Button>;
		}
		if (type === 'jump-prev' || type === 'jump-next') {
			return <Button>...</Button>;
		}
		return originalElement;
	};

	return (
		<div className={style.pagination}>
			<AntPagination
				size="small"
				pageSize={PAGE_SIZE}
				total={Number(count)}
				current={Number(page)}
				showTotal={() => null}
				prefixCls="pagination"
				showSizeChanger={false}
				itemRender={itemRender}
				showQuickJumper={false}
				onChange={handlePageChange}
				prevIcon={<DoubleLeftOutlined />}
				nextIcon={<DoubleRightOutlined />}
			/>
		</div>
	);
};

export default Pagination;
