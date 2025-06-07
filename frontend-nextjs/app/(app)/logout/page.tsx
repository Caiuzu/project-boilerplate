'use client';

import React from 'react';

import { useSession } from '~/app/usecase/contexts/AuthContext';

const LogoutPage = () => {
	const { logout } = useSession();

	React.useEffect(() => {
		logout();
	}, []);

	return <></>;
};

export default LogoutPage;
