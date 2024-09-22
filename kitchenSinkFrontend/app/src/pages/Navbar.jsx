import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../Context/LoginContext';
import { Button } from 'react-bootstrap';

const Navbar = () => {

    const {user, login, logout} = useAuth();


    const handleLogout = () => {
        console.log('logging out');
        logout();
    }

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <Link className="navbar-brand" to="/">Kitchensink</Link>

            <div className=" navbarbuttons" id="navbarNav">
            {user === null ? (
                <ul className="navbar-nav ml-auto ">
                    <li className="nav-item navbarlink">
                        <Link className="nav-link" to="/">Sign In</Link>
                    </li>
                    <li className="nav-item navbarlink">
                        <Link className="nav-link" to="/signup">Sign Up</Link>
                    </li>
                </ul>) : (
                    <ul className="navbar-nav ml-auto ">
                    <li className="nav-item navbarlink">
                        <Button variant='primary' onClick={handleLogout}>logout</Button>
                    </li>
                </ul>
                )}
            </div>
        </nav>
    );
};

export default Navbar;