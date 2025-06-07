import type { Metadata } from 'next';

import React from 'react';

import PrivacyPage from '~/components/Privacy';

export const metadata: Metadata = {
	title: 'Política de Privacidade'
};

const Privacy = () => {
	return <PrivacyPage />;
};

export default Privacy;
