import { ThemeConfig, theme as themeConfig } from 'antd';

export const theme: ThemeConfig = {
	algorithm: themeConfig.darkAlgorithm,
	components: {
		Input: {
			controlHeight: 40
		},
		Select: {
			controlHeight: 40
		},
		Button: {
			lineHeight: 1,
			borderRadius: 4,
			fontWeight: 500,
			controlHeight: 40
		}
	},
	token: {
		borderRadius: 4,
		controlHeight: 36,
		colorLink: '#3068ba',
		colorError: '#bb3031',
		colorBgBase: '#141414',
		colorPrimary: '#3068ba',
		colorLinkHover: '#214882',
		colorLinkActive: '#214882',
		colorBgContainer: '#1e1e1e',
		colorPrimaryHover: '#214882',
		colorPrimaryActive: '#214882'
	}
};
