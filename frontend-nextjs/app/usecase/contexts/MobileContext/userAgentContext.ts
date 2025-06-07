import React from 'react';
import { userAgent } from 'next/server';

export type UserAgent = ReturnType<typeof userAgent>;
export const UserAgentContext = React.createContext<UserAgent | undefined>(undefined);
