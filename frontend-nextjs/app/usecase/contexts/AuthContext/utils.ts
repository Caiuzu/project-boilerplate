import { cookies } from 'next/headers';

import { SESSION_COOKIE } from '~/app/usecase/util/Constants';
import { TokenResponse } from '~/app/domain/interface/user.interface';

export const getUser = async (): Promise<TokenResponse> => {
	const session = (await cookies()).get(SESSION_COOKIE) || { value: '{}' };
	return JSON.parse(session.value);
};
