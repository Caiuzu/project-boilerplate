'use client';

import React from 'react';
import { Card, Space, Typography } from 'antd';

const { Text, Link, Title, Paragraph } = Typography;

const PrivacyPage = () => {
	return (
		<Card style={{ maxWidth: 800, margin: '0 auto' }}>
			<Typography>
				<Title level={1}>Política De Privacidade</Title>

				<Title level={2}>Nosso compromisso com a privacidade</Title>
				<Paragraph>
					Sua privacidade é importante para nós. Para melhor proteger sua privacidade nós fornecemos esta nota explicando nossas práticas de
					informação online e as escolhas que você pode fazer sobre a maneira como suas informações são coletadas e utilizadas. Para tornar
					esta declaração fácil de encontrar, tornamos-a disponível em nossa página de regras para a comunidade. Você pode solicitar, a
					qualquer momento, ações para defender seus interesses de privacidade.
				</Paragraph>

				<Title level={2}>A informação que coletamos</Title>
				<Paragraph>
					Este aviso é aplicável a todas as informações coletadas ou apresentadas em nossa comunidade. Em algumas páginas, você pode ler o
					conteúdo e se registrar para interagir com a comunidade. Os tipos de informações pessoais coletadas no registro são:
				</Paragraph>
				<ul>
					<li>E-mail</li>
				</ul>

				<Title level={2}>A forma como usamos a Informação</Title>
				<Paragraph>
					Usamos os endereços de e-mail para validade de contas e para proporcionar uma melhor experiência em nosso site. Nós nunca
					compartilharemos as informações pessoais que você nos fornece.
				</Paragraph>

				<Title level={2}>Como você pode acessar ou corrigir as informações</Title>
				<Paragraph>
					Você pode acessar todas as suas informações pessoalmente identificáveis que coletamos e configurar por conta própria. Para
					proteger a sua privacidade e segurança, também tomaremos medidas razoáveis para verificar sua identidade antes de conceder acesso
					ou fazer correções.
				</Paragraph>

				<Title level={2}>Como entrar em contato conosco</Title>
				<Paragraph>
					Se você tiver outras dúvidas ou preocupações sobre essas políticas de privacidade, fale conosco em{' '}
					<Link href="mailto:contato@email.com">contato@email.com</Link>.
				</Paragraph>

				<Title level={2}>Third Parties</Title>
				<Space direction="vertical">
					<div>
						<Title level={3}>SendGrid</Title>
						<Paragraph>
							SendGrid is used by this website to process and deliver emails.{' '}
							<Link target="_blank" href="https://sendgrid.com/policies/privacy/">
								Privacy Policy
							</Link>
						</Paragraph>
					</div>

					<div>
						<Title level={3}>reCAPTCHA V2</Title>
						<Paragraph>
							This site uses a CAPTCHA to ensure humans are performing certain actions. The CAPTCHA provider may set a session cookie and
							get information about your internet browser and device accessing this website.{' '}
							<Link target="_blank" href="https://www.google.com/policies/privacy/">
								Privacy Policy
							</Link>
						</Paragraph>
					</div>
				</Space>
			</Typography>
		</Card>
	);
};

export default PrivacyPage;
