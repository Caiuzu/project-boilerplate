interface LastPoster {
	id: number;
	name: string;
	title: string;
	postId: number;
	seoTitle: string;
	updatedAt: number;
	thumbnail: string;
}

interface BoardPost {
	id: number;
	title: string;
	seoTitle: string;
	perms?: string[];
	unread?: boolean;
	position?: number;
	postsCount: number;
	topicsCount: number;
	description?: string;
	lastPostDate?: number;
	lastPoster: LastPoster;
}

export interface BoardCategory extends BoardPost {
	children?: BoardCategory[];
}

export interface ReducedPostAuthorResponse {
	id: number;
	name: string;
	avatar: string;
}

export interface PostAuthorResponse extends ReducedPostAuthorResponse {
	title: string;
	gender: string;
	joinedAt: number;
	iamImage: string;
	signature: string;
	postCount: number;
	groupIcon: string;
	groupName: string;
	tibiaCharacter: string;
}

export interface Attachment {
	id: number;
	fileName: string;
	fileSize: number;
	extension: string;
	downloadCount: number;
}

export interface PostResponse {
	id: number;
	content: string;
	postedAt: number;
	author: PostAuthorResponse;
	attachments?: Attachment[];
}

export interface TopicResponse {
	id: number;
	title: string;
	state: string;
	seoTitle: string;
	postedAt: number;
	isPinned: boolean;
	isFeatured: boolean;
	posts: PostResponse[];
	parent: ParentCategory;
	author: ReducedPostAuthorResponse;
}

export interface PortalTopicResponse extends Omit<TopicResponse, 'posts' | 'parent'> {
	content: string;
}

export interface PortalBlockResponse {
	name: string;
	title: string;
	block_id: number;
	template: number;
	block_code: string;
	show_block: number;
}

export interface BoardMenuResponse {
	id: number;
	app: string;
	config: string;
	parent: number;
	title?: string;
	position: number;
	extension: string;
	is_menu_child: number;
	permissions: null | string;
	children: BoardMenuResponse[];
}

export interface ParentCategory {
	id: number;
	title: string;
	seoTitle: string;
}

interface LastPostPoster {
	id: number;
	name: string;
	postId: number;
	updatedAt: number;
	thumbnail: string;
}

export interface TopicStructure {
	id: number;
	views: number;
	posts: number;
	title: string;
	state: string;
	unread: boolean;
	titleSeo: string;
	postedAt: number;
	isPinned?: boolean;
	isLocked?: boolean;
	isFeatured?: boolean;
	lastPoster: LastPostPoster;
	author: ReducedPostAuthorResponse;
}

export interface LastUpdatedTopicStructure extends Omit<TopicStructure, 'unread'> {
	lastPoster: LastPostPoster;
}

export interface SubCategoryStructure {
	id: number;
	title: string;
	seoTitle: string;
	unread?: boolean;
	postsCount: number;
	topicsCount: number;
	description?: string;
	lastPoster: LastPoster;
}

export interface CategoryResponse {
	id: number;
	title: string;
	description?: string;
	parent: ParentCategory;
	topics: TopicStructure[];
	subCategories?: SubCategoryStructure[];
}
