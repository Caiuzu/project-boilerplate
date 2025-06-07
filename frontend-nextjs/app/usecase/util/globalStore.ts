interface Store {
	[key: string]: any;
}

let store: Store = {};

interface GlobalStore {
	get: (key: string) => any;
	set: (key: string, value: any) => void;
}

const globalStore: GlobalStore = {
	get: (key: string): any => {
		return store[key];
	},
	set: (key: string, value: any): void => {
		store = { ...store, [key]: value };
	}
};

export default globalStore;
