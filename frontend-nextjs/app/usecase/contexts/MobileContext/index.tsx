import React from 'react';

import { UserAgentContext } from './userAgentContext';

export const useIsMobile = () => {
	const userAgent = React.useContext(UserAgentContext);
	const [isMobile, setIsMobile] = React.useState(userAgent?.device.type === 'mobile');

	if (!userAgent) {
		throw new Error('useIsMobile must be used within a UserAgentContext.Provider');
	}

	React.useEffect(() => {
		const checkMobile = () => setIsMobile(window.innerWidth <= 768);

		window.addEventListener('resize', checkMobile);
		return () => window.removeEventListener('resize', checkMobile);
	}, []);

	return isMobile;
};
