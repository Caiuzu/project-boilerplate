export interface TokenResponse {
	user: User;
	token: string;
	expires: number;
}

export interface User {
	id: number;
	name: string;
	email: string;
	group: number;
	avatar: string;
}

export interface LoginRequest {
	username: string;
	password: string;
}
