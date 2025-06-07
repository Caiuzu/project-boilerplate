import type { NextRequest, NextResponse } from 'next/server';

import { ReactNode } from 'react';

import { TokenResponse } from '~/app/domain/interface/user.interface';

export interface AuthContext {
	logout: () => void;
	session: TokenResponse;
}

export interface AuthProviderProps {
	children: ReactNode;
	appAuth: TokenResponse;
}

export type AppRequestOptions = { req?: Request | NextRequest; res?: Response | NextResponse };
