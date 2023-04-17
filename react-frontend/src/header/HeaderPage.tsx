import React from 'react';
import styled from 'styled-components';

const Header = styled.header`
display: flex;
justify-content: space-between;
align-items: center;
padding: 0.5rem;
background-color: #ffffff;
box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
`;

const Logo = styled.img`
height: 4rem;
`;

const Nav = styled.nav`
display: flex;
`;

const NavLink = styled.a`
color: #333333;
text-decoration: none;
margin-right: 1rem;
font-weight: bold;
&:hover {
 color: #666666;
 }
`;

export default function HeaderPage() {
    return (
        <Header>
            <Logo src="src/assets/zuhlke-logo-transparent.png" alt="Logo" />
            <Nav>
                <NavLink href="rules">Rules</NavLink>
                <NavLink href="tournament">Tournament</NavLink>
                <NavLink href="/">Main</NavLink>
            </Nav>
        </Header>
    );
}
