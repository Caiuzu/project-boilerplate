export const EMPTY_STRING = '';
export const SESSION_COOKIE = 'userSession';
export const NEXT_LOCALE = 'NEXT_LOCALE';
export const LOGIN_PATH = '/login';
export const ROOT_PATH = '/';
export const PAGE_SIZE = 15;
export const ADMIN_GROUP = 4;
export const BOARD_PATH = '/board';
export const DATE_FORMAT_FORM = 'DD / MMM / YYYY';
export const EMAIL_SENT_PATH = '/email/sent';
export const EMAIL_CONFIRMATION_PATH = '/email/confirmation/';
export const CREDENTIALS_PATH = '/credentials';
export const SIGN_UP_PATH = '/signup';
export const EMPTY_RESPONSE = {
	items: [],
	totalPages: 1,
	totalItems: 0,
	currentPage: 0
};

export const TYPE_COLORS: { [key: string]: string } = {
	WEIGHT: '#ba6a30',
	DOCUMENT: '#1F992B',
	PATHOLOGY: '#BA309C',
	APPOINTMENT: '#3068ba',
	OBSERVATION: '#545454',
	PRESCRIPTION: '#6d30ba',
	RENEW_REPORT: '#BA3030',
	MEDICAL_REPORT: '#388E3C',
	HOSPITALIZATION: '#BA3030',
	DISCHARGE_REPORT: '#5BB033'
};

export enum Language {
	PT = 'PT',
	EN = 'EN'
}

export const datePickerFormat = 'DD/MM/YYYY';
