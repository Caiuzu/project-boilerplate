import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

export const formatDate = (timestamp: number, formatStr: string = 'MMMM dd, yyyy'): string => {
	const date = new Date(timestamp * 1000);
	return format(date, formatStr, { locale: ptBR });
};
