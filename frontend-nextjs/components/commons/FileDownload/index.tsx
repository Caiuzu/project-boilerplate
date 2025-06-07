import styles from './styles.module.scss';
import { formatBytes } from '~/app/usecase/util/stringUtils';
import { Attachment } from '~/app/domain/interface/board.interface';

const FileDownload = ({ url, attachment }: { url: string; attachment: Attachment }) => (
	<div className={styles.fileDownload}>
		<a href={url} target="_blank" className="file-link" rel="noopener noreferrer" />
		<div className={styles.fileIcon}>
			<svg width="32" height="32" viewBox="0 0 256 256" xmlns="http://www.w3.org/2000/svg">
				<rect width="256" fill="none" height="256" />
				<path
					fill="none"
					stroke-width="16"
					stroke="currentColor"
					stroke-linecap="round"
					stroke-linejoin="round"
					d="M200,224H56a8,8,0,0,1-8-8V40a8,8,0,0,1,8-8h96l56,56V216A8,8,0,0,1,200,224Z"
				/>
				<polyline
					fill="none"
					stroke-width="16"
					stroke="currentColor"
					stroke-linecap="round"
					stroke-linejoin="round"
					points="152 32 152 88 208 88"
				/>
				<line
					x1="128"
					y1="120"
					x2="128"
					y2="184"
					fill="none"
					stroke-width="16"
					stroke="currentColor"
					stroke-linecap="round"
					stroke-linejoin="round"
				/>
				<polyline
					fill="none"
					stroke-width="16"
					stroke="currentColor"
					stroke-linecap="round"
					stroke-linejoin="round"
					points="104 160 128 184 152 160"
				/>
			</svg>
		</div>
		<div className={styles.fileInfo}>
			<div className={styles.fileName}>{attachment.fileName}</div>
			<div className={styles.fileDetails}>
				<span>{formatBytes(attachment.fileSize, 2)}</span>
				<span className={styles.bullet}>•</span>
				<span>{attachment.downloadCount} downloads</span>
			</div>
		</div>
	</div>
);

export default FileDownload;
