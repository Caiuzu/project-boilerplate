'use client';
import type { MessageContext, MessageProviderProps } from './types';

import React from 'react';
import { message } from 'antd';

const MessageContext = React.createContext({} as MessageContext);

export const MessageProvider = ({ children }: MessageProviderProps) => {
	const [msg, contextHolder] = message.useMessage();

	return (
		<MessageContext.Provider value={{ msg }}>
			{contextHolder}
			{children}
		</MessageContext.Provider>
	);
};

export const useMessage = () => React.useContext(MessageContext);
