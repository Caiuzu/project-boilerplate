import { NextRequest, NextResponse } from 'next/server';

import { SESSION_COOKIE } from '~/app/usecase/util/Constants';

export default async function middleware(request: NextRequest) {
	if (request.cookies.has(SESSION_COOKIE)) {
		const session = request.cookies.get(SESSION_COOKIE);
		const jsonSession = JSON.parse(session?.value!);

		if (jsonSession && jsonSession.expires < Date.now()) {
			const response = NextResponse.redirect(new URL('/', request.nextUrl.toString()).href);
			response.cookies.delete(SESSION_COOKIE);
			return response;
		}
	}
}

export const config = {
	matcher: ['/((?!api|_next|.*\\..*).*)']
};
