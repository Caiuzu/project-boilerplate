import { Rule } from 'rc-field-form/lib/interface';

import { LoginRequest } from '~/app/domain/interface/user.interface';

export const initialValues: LoginRequest = {
	username: '',
	password: ''
};

export const passRules = [{ required: true, message: 'Por favor, insira sua senha' }] as Rule[];
export const emailRules = [{ required: true, message: 'Por favor, insira um email válido' }] as Rule[];
