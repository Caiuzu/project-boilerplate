import { BoardCategory, TopicStructure } from '~/app/domain/interface/board.interface';

export interface HomePageProps {
	data: BoardCategory[];
	latestTopics: TopicStructure[];
}
