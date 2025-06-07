export interface PaginatedResponse<T> {
	items: T[];
	totalPages: number;
	totalItems: number;
	currentPage: number;
}

export interface SinglePaginatedResponse<T> {
	items: T;
	totalPages: number;
	totalItems: number;
	currentPage: number;
}

export class PaginatedQueryRequest<T> {
	query?: T;
	order?: string;
	orderBy?: string;
	pageSize: number = 15;
	pageNumber: number = 0;

	constructor(query: T) {
		this.pageSize = 15;
		this.pageNumber = 0;
		this.query = query;
		this.order = 'DESC';
		this.orderBy = '';
	}
}

export class PaginatedDatabaseQuery {
	where: any;
	skip: number;
	take?: number;
	order?: string;
	orderBy?: string;

	constructor(paginatedQuery: PaginatedQueryRequest<any>) {
		this.skip = paginatedQuery.pageSize * paginatedQuery.pageNumber;
		if (paginatedQuery.pageSize !== -1) this.take = paginatedQuery.pageSize;
		this.order = paginatedQuery.order;
		this.orderBy = paginatedQuery.orderBy;
	}
}
