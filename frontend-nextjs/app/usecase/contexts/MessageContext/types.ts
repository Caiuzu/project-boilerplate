import React from 'react';
import { MessageInstance } from 'antd/lib/message/interface';

export interface MessageContext {
	msg: MessageInstance;
}

export interface MessageProviderProps {
	children: React.ReactNode;
}
