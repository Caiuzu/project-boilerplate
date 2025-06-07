const path = require('path');
const createNextIntlPlugin = require('next-intl/plugin');
const withNextIntl = createNextIntlPlugin();

/** @type {import('next').NextConfig} */
const nextConfig = {
	output: 'standalone',
	reactStrictMode: false,
	generateBuildId: async () => {
		return 'build-' + Date.now();
	},
	compiler: {
		removeConsole: process.env.NODE_ENV === 'production'
	},
	experimental: {
		optimizeCss: true,
		optimizePackageImports: ['antd']
	},
	env: {
		SERVER_URL: process.env.SERVER_URL,
		APP_URL: process.env.NEXT_PUBLIC_APP_URL
	},
	sassOptions: {
		includePaths: ['.'],
		prependData: `@use "~/styles/mixins.scss" as *;`
	}
};

module.exports = withNextIntl(nextConfig);
