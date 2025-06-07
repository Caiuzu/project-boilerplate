import React from 'react';
import DOMPurify from 'isomorphic-dompurify';

import FileDownload from '~/components/commons/FileDownload';
import { Attachment } from '~/app/domain/interface/board.interface';

export const removeAccents = (str: string) => {
	return str
		.normalize('NFD')
		.replace(/[\u0300-\u036f]/g, '')
		?.toLowerCase();
};

export const getInitials = (name: string): string => {
	const ignoreList = ['de', 'da', 'do', 'dos', 'das'];
	const nameParts = name.split(' ').filter((part) => !ignoreList.includes(part.toLowerCase()));
	const [firstName, lastName] = nameParts;
	return `${firstName.charAt(0)}${lastName ? lastName.charAt(0) : ''}`;
};

export const getStringNumbers = (string: null | string) => {
	return string?.replace(/\D/g, '');
};

export const isUUID = (input: string) => {
	const uuidRegex = /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/;
	return uuidRegex.test(input);
};

export const getUserImageUrl = (url?: string): string => {
	return url ? `https://cdn.kit.com/${url}` : '/img/default_photo.png';
};

export const isNotEmpty = (value?: unknown) => {
	if (!value && typeof value !== 'number') return;
	return value?.toString() && value?.toString().length !== 0;
};

export const sanitize = (content: string): string => {
	return DOMPurify.sanitize(content);
};

export const numFormatter = (num: number): string => {
	if (num > 999 && num < 1000000) {
		return (num / 1000).toFixed(1) + 'K';
	} else if (num > 1000000) {
		return (num / 1000000).toFixed(1) + 'M';
	} else {
		return num.toString();
	}
};

export const fixContentLinks = (content: null | string) => {
	if (!content) return null;
	const fixedEmote = content.replace(/<fileStore.core_Emoticons>/g, 'https://cdn.kit.com');
	const fixedBaseUrl = fixBaseUrl(fixedEmote);
	return fixedBaseUrl.replace(/<fileStore.core_Attachment>/g, 'https://cdn.kit.com');
};

export const fixBlockLinks = (content: string) => {
	const urlRegex = /https?:\/\/(www\.)?kit\.com/g;
	return content.replace(urlRegex, 'https://beta.kit.com');
};

export const fixBaseUrl = (content: string) => {
	const urlRegex = /<___base_url___>/g;
	return content.replace(urlRegex, 'https://cdn.kit.com');
};

export const fixDownloadLinks = (content: string) => {
	const urlRegex = /<___base_url___>\/applications\/core\/interface\/file\/attachment\.php\?id=(\d+)(?:&key=([^&]+))?/g;
	return content.replace(urlRegex, (match, id, key) => {
		const keyParam = key ? `&key=${key}` : '';
		return `${process.env.APP_URL}/api/attachment?id=${id}${keyParam}`;
	});
};

const renderComponentToString = async (Component: React.ReactElement) => {
	const ReactDOMServer = await import('react-dom/server');
	return ReactDOMServer.renderToString(Component);
};

export const fixDownloadNode = async (content: string, attachments?: Attachment[]) => {
	const downloadNodes = content.match(/<a\b[^>]*data-fileid="(\d+)"[^>]*>(?:[^<]|<(?!\/a>))*<\/a>/g);

	if (downloadNodes) {
		const replacementPromises = downloadNodes.map(async (node) => {
			const fileExt = node.match(/data-fileext="([^"]+)"/)?.[1];
			const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg', 'bmp'];

			if (fileExt && imageExtensions.includes(fileExt.toLowerCase())) {
				return null;
			}

			const href = node.match(/href="([^"]+)"/)?.[1];
			const fileId = node.match(/data-fileid="([^"]+)"/)?.[1];
			const attachment = attachments?.find((attachment) => Number(attachment.id) === Number(fileId));

			const key = href?.match(/key=([^&]+)/)?.[1];
			const newUrl = `${process.env.APP_URL}/api/attachment?id=${fileId}${key ? `&key=${key}` : ''}`;

			if (attachment) {
				const downloadComponent = await renderComponentToString(<FileDownload url={newUrl} attachment={attachment} />);
				return { node, replacement: downloadComponent };
			}
			return null;
		});

		const replacements = await Promise.all(replacementPromises);

		let updatedContent = content;
		replacements.forEach((replacement) => {
			if (replacement) {
				updatedContent = updatedContent.replace(replacement.node, replacement.replacement);
			}
		});

		return updatedContent;
	}

	return content;
};

export const formatBytes = (input: number | string, decimals = 3): string => {
	let bytes: number;

	if (typeof input === 'string') {
		bytes = parseInt(input);
		if (isNaN(bytes)) {
			return '0 Bytes';
		}
	} else {
		bytes = input;
	}

	if (isNaN(bytes) || bytes === 0) return '0 Bytes';

	const k = 1024;
	const dm = decimals < 0 ? 0 : decimals;
	const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
	const i = Math.floor(Math.log(bytes) / Math.log(k));

	return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
};

export function getProfileLink(id?: string | number, name?: string) {
	if (!id) return '/profile';
	if (!name) return `/profile/${id}`;
	return `/profile/${id}-${encodeURIComponent(name)}`;
}
