import NProgress from 'nprogress';
import { useCallback } from 'react';
import { usePathname, useRouter as useNextRouter } from 'next/navigation';
import { NavigateOptions } from 'next/dist/shared/lib/app-router-context.shared-runtime';

import globalStore from '~/app/usecase/util/globalStore';

export const useRouter = () => {
	const router = useNextRouter();
	const pathname = usePathname();

	const getBasePath = (path: string) => {
		const pageMatch = path.match(/(.*?)\/page\/\d+$/);
		return pageMatch ? pageMatch[1] : path;
	};

	const saveScrollPosition = () => {
		const basePath = getBasePath(pathname);
		globalStore.set(`scroll_${basePath}`, window.scrollY);
	};

	const push = useCallback(
		(href: string, options?: NavigateOptions) => {
			const currentBasePath = getBasePath(pathname);
			const newBasePath = getBasePath(href);

			if (currentBasePath !== newBasePath) {
				saveScrollPosition();
				NProgress.start();
			}
			router.push(href, options);
			router.refresh();
		},
		[router, pathname]
	);

	const replace = useCallback(
		(href: string, options?: NavigateOptions) => {
			const currentBasePath = getBasePath(pathname);
			const newBasePath = getBasePath(href);

			if (currentBasePath !== newBasePath) {
				saveScrollPosition();
				NProgress.start();
			}
			router.replace(href, options);
			router.refresh();
		},
		[router, pathname]
	);

	return {
		...router,
		push,
		replace
	};
};
