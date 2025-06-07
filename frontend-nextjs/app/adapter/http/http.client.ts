import { redirect, RedirectType } from 'next/navigation';

export class HttpClient {
	apiURL: string;

	constructor(apiUrl: string) {
		this.apiURL = apiUrl;
	}

	private getUrl(path: string) {
		return `${this.apiURL}${path}`;
	}

	async delete(path: string, token?: string) {
		const requestOptions = this.getOptions('DELETE', token);
		return fetch(this.getUrl(path), requestOptions).then(this.handleResponse).catch(this.handleError);
	}

	async patch<K>(path: string, body: K, token?: string) {
		const requestOptions = this.getOptions('PATCH', token, body);
		return fetch(this.getUrl(path), requestOptions).then(this.handleResponse).catch(this.handleError);
	}

	async upload(path: string, body: FormData, token?: string) {
		const requestOptions = this.getOptions('POST', token, body);
		return fetch(this.getUrl(path), requestOptions).then(this.handleResponse).catch(this.handleError);
	}

	async get(path: string, token?: string, cache: RequestCache = 'no-cache') {
		const requestOptions = this.getOptions('GET', token, undefined, cache);
		return fetch(this.getUrl(path), requestOptions).then(this.handleResponse).catch(this.handleError);
	}

	async put<K>(path: string, body: K, token?: string, cache: RequestCache = 'no-cache') {
		const requestOptions = this.getOptions('PUT', token, body, cache);
		return fetch(this.getUrl(path), requestOptions).then(this.handleResponse).catch(this.handleError);
	}

	async post<K>(path: string, body: K, token?: string, cache: RequestCache = 'no-cache') {
		const requestOptions = this.getOptions('POST', token, body, cache);
		return fetch(this.getUrl(path), requestOptions).then(this.handleResponse).catch(this.handleError);
	}

	private getOptions(method: string, token?: string, body?: any, cache?: RequestCache): RequestInit {
		const headers = new Headers();
		if (body) headers.append('Content-Type', 'application/json');
		if (token) headers.append('Authorization', `Bearer ${token}`);

		return { cache, method, headers, body: JSON.stringify(body) };
	}

	private handleResponse(response: Response) {
		if (response.status === 204) {
			return {
				items: [],
				totalPages: 0,
				totalItems: 0,
				currentPage: 0
			};
		} else if (response.status === 401) {
			return Promise.reject({ code: 401, message: 'Unauthorized' });
		} else if (response.status >= 400 && response.status < 500) {
			return response.json().then((data) => {
				const error = (data && data.message) || response.statusText;
				return Promise.reject(error);
			});
		} else if (response.status >= 500 && response.status < 600) {
			return response.json().then((data) => {
				const error = (data && data.message) || response.statusText;
				return Promise.reject(error);
			});
		} else {
			return response.text().then((text) => {
				return text && JSON.parse(text);
			});
		}
	}

	private handleError(error: any) {
		if (error.code === 401) {
			redirect('/logout', RedirectType.push);
		}
	}
}
