import React from "react";
import styled from "styled-components";
import logo from "/src/assets/zuhlke-logo-transparent.png"

const HeaderSection = styled.header`
  display: flex;
  align-items: center;
  padding: 0.5rem;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
`;

const Logo = styled.img`
  height: 4rem;
  float: left;
`;

const Nav = styled.nav`
  display: flex;
  float: right;
  margin-left: auto;
`;

const NavLink = styled.a`
  text-decoration: none;
  margin-right: 1rem;
  font-weight: bold;
  float: right;
  margin-left: auto;
`;

const Title = styled.h1`
  font-size: 1.5rem;
  padding-left: 1rem;
  margin: 0 0 0 0;
`;

export default function Header() {
    return (
        <HeaderSection>
            <Logo src={logo} alt="Logo"/>
            <Title>Tablut Coding Challenge</Title>
            <Nav>
                <NavLink href="/">Home</NavLink>
                <NavLink href="/rules">Rules</NavLink>
                <NavLink href="/tournament">Tournament</NavLink>
            </Nav>
        </HeaderSection>
    );
}
