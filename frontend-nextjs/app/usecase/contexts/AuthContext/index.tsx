import type { AuthContext, AuthProviderProps } from './types';

import React from 'react';
import { deleteCookie } from 'cookies-next';

import { useRouter } from '~/app/usecase/hooks/useRouter';
import { SESSION_COOKIE } from '~/app/usecase/util/Constants';

const AuthContext = React.createContext({} as AuthContext);

export const AuthProvider = ({ appAuth, children }: AuthProviderProps) => {
	const router = useRouter();
	const session = appAuth;

	const logout = () => {
		deleteCookie(SESSION_COOKIE);
		router.refresh();
		router.push('/');
	};

	return <AuthContext.Provider value={{ logout, session }}>{children}</AuthContext.Provider>;
};
export const useSession = () => React.useContext(AuthContext);
