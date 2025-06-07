import React from 'react';
import { usePathname } from 'next/navigation';

import globalStore from '~/app/usecase/util/globalStore';

export const useScrollPosition = () => {
	const pathname = usePathname();

	const getBasePath = (path: string) => {
		const pageMatch = path.match(/(.*?)\/page\/\d+$/);
		return pageMatch ? pageMatch[1] : path;
	};

	React.useEffect(() => {
		const basePath = getBasePath(pathname);

		const scrollPosition = globalStore.get(`scroll_${basePath}`);
		if (scrollPosition) {
			window.scrollTo(0, scrollPosition);
		}

		const handleScroll = () => {
			globalStore.set(`scroll_${basePath}`, window.scrollY);
		};

		window.addEventListener('scroll', handleScroll);
		return () => window.removeEventListener('scroll', handleScroll);
	}, [pathname]);
};
