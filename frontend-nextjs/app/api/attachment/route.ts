import { NextRequest, NextResponse } from 'next/server';

export async function GET(request: NextRequest) {
	try {
		const searchParams = request.nextUrl.searchParams;
		const fileId = searchParams.get('id');
		const key = searchParams.get('key');

		if (!fileId) {
			return NextResponse.json({ error: 'File ID is required' }, { status: 400 });
		}

		const serverUrl = process.env.SERVER_URL;
		if (!serverUrl) {
			return NextResponse.json({ error: 'Server URL not configured' }, { status: 500 });
		}

		const url = new URL(`${process.env.SERVER_URL}/files/${fileId}`);
		if (key) {
			url.searchParams.append('key', key);
		}

		const response = await fetch(url.toString(), {
			method: 'GET',
			headers: {
				Accept: 'application/octet-stream'
			}
		});

		if (!response.ok) {
			return NextResponse.json({ error: 'File not found or server error' }, { status: response.status });
		}

		const headers = new Headers();
		const contentType = response.headers.get('content-type');
		const contentDisposition = response.headers.get('content-disposition');
		const contentLength = response.headers.get('content-length');

		if (contentType) headers.set('Content-Type', contentType);
		if (contentDisposition) headers.set('Content-Disposition', contentDisposition);
		if (contentLength) headers.set('Content-Length', contentLength);

		return new NextResponse(response.body, {
			headers,
			status: 200
		});
	} catch (error) {
		console.error('Download error:', error);
		return NextResponse.json({ error: 'Failed to download file' }, { status: 500 });
	}
}
