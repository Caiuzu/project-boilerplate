export type Props = {
	params: Promise<any>;
	searchParams: Promise<{ [key: string]: string | string[] | undefined } & SearchParams>;
};

export type SearchParams = {
	page?: string;
};
