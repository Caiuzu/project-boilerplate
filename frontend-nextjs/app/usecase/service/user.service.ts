import { HttpClient } from '~/app/adapter/http/http.client';
import { LoginRequest, TokenResponse } from '~/app/domain/interface/user.interface';

class Service {
	private readonly authPath = '/auth';
	private readonly basePath = '/users';
	private readonly _loginPath = '/login';
	private _serverClient = new HttpClient(process.env.SERVER_URL as string);

	forgot(email: string) {
		return this._serverClient.post(`${this.authPath}/forgot`, { email });
	}

	renew(id: string, token: string) {
		return this._serverClient.post(`${this.authPath}/renew`, { id }, token);
	}

	getUserProfile(id: string, token: string): Promise<any> {
		return this._serverClient.get(`${this.basePath}/${id}/profile`, token);
	}

	reset(token: string, password: string) {
		return this._serverClient.post(`${this.authPath}/reset`, { token, password });
	}

	async login(request: LoginRequest): Promise<TokenResponse> {
		return await this._serverClient.post<LoginRequest>(`${this.authPath}${this._loginPath}`, request);
	}
}

export const userService = new Service();
