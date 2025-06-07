import { WaveConfig } from 'antd/es/config-provider/context';

const createHolder = (node: HTMLElement) => {
	const { borderWidth } = getComputedStyle(node);
	const borderWidthNum = parseInt(borderWidth, 10);

	const div = document.createElement('div');
	div.style.position = 'absolute';
	div.style.inset = `-${borderWidthNum}px`;
	div.style.borderRadius = 'inherit';
	div.style.background = 'transparent';
	div.style.zIndex = '999';
	div.style.pointerEvents = 'none';
	div.style.overflow = 'hidden';
	node.appendChild(div);

	return div;
};

const createDot = (holder: HTMLElement, color: string, left: number, top: number, size = 0) => {
	const dot = document.createElement('div');
	dot.style.position = 'absolute';
	dot.style.left = `${left}px`;
	dot.style.top = `${top}px`;
	dot.style.width = `${size}px`;
	dot.style.height = `${size}px`;
	dot.style.borderRadius = '50%';
	dot.style.background = color;
	dot.style.transform = 'translate(-50%, -50%)';
	dot.style.transition = 'all 500ms ease-out';
	holder.appendChild(dot);

	return dot;
};

const calculateLuminance = (r: number, g: number, b: number, a: number) => {
	const aRGB = [r, g, b].map((v) => {
		v /= 255;
		return v <= 0.03928 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4);
	});
	return (aRGB[0] * 0.2126 + aRGB[1] * 0.7152 + aRGB[2] * 0.0722) * a;
};

const adjustColorBrightness = (r: number, g: number, b: number, factor: number) => {
	return [Math.min(255, Math.max(0, r + factor)), Math.min(255, Math.max(0, g + factor)), Math.min(255, Math.max(0, b + factor))];
};

const extractRGBA = (color: string) => {
	return color
		.match(/rgba?\((\d+), (\d+), (\d+), (\d+(\.\d+)?)\)/)
		?.slice(1)
		.map(Number);
};

const extractRGB = (color: string) => {
	return color
		.match(/rgb?\((\d+), (\d+), (\d+)\)/)
		?.slice(1, 4)
		.map(Number);
};

export const showInsetEffect: WaveConfig['showEffect'] = (node, { event, component }) => {
	if (component !== 'Button') return;

	const backgroundColor = getComputedStyle(node).backgroundColor;
	const color = extractRGBA(backgroundColor) ?? extractRGB(backgroundColor)?.map(Number);
	let [r, g, b, a = 1] = color || [255, 255, 255, 1];

	const luminance = calculateLuminance(r, g, b, a);
	let colorString;

	if (luminance < 0.5 && a < 1) {
		colorString = `rgba(${r}, ${g}, ${b}, 0.8)`;
	} else {
		const factor = luminance < 0.5 ? 100 : -100;
		[r, g, b] = adjustColorBrightness(r, g, b, factor);
		colorString = `rgba(${r}, ${g}, ${b}, 0.5)`;
	}

	const holder = createHolder(node);
	const rect = holder.getBoundingClientRect();

	const left = event.clientX - rect.left;
	const top = event.clientY - rect.top;

	const dot = createDot(holder, colorString, left, top, 0);

	requestAnimationFrame(() => {
		dot.ontransitionend = () => {
			holder.remove();
		};

		dot.style.width = '200px';
		dot.style.height = '200px';
		dot.style.opacity = '0';
	});
};
