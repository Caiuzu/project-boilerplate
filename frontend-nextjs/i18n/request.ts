import { getRequestConfig } from 'next-intl/server';

import ptMessages from '~/public/locales/pt.json';
import enMessages from '~/public/locales/en.json';

const messagesMap: { [key: string]: any } = {
	pt: ptMessages,
	en: enMessages
};

export default getRequestConfig(async ({ requestLocale }) => {
	const locale = 'pt';

	return {
		locale,
		messages: messagesMap[locale]
	};
});
