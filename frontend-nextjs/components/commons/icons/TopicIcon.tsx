import React from 'react';

interface TopicIconProps {
	size?: number;
	className?: string;
	weight?: 'fill' | 'outline';
	children: React.ReactElement<{ size?: number; color?: string; weight?: 'fill' | 'outline' }>;
}

const TopicIcon = ({ children, size = 16, className, weight = 'fill' }: TopicIconProps) => {
	return (
		<div
			className={`icon-wrapper ${className || ''}`}
			style={{
				padding: '6px',
				display: 'flex',
				borderRadius: '25px',
				width: 'fit-content',
				alignItems: 'center',
				justifyContent: 'center',
				backgroundColor: '#0e2f26'
			}}
		>
			{React.isValidElement(children) &&
				React.cloneElement(children, { ...children.props, size, weight, color: children.props.color || '#4bb15c' })}
		</div>
	);
};

export default TopicIcon;
