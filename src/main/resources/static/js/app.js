// Main React application for Spotify User Profile

// UserTracks component to display user's top tracks
const UserTracks = () => {
    const [tracks, setTracks] = React.useState(null);
    const [loading, setLoading] = React.useState(true);
    const [error, setError] = React.useState(null);

    React.useEffect(() => {
        // Fetch user tracks data from the Spring Boot backend
        fetch('/api/users/tracks')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch tracks');
                }
                return response.json();
            })
            .then(data => {
                setTracks(data);
                setLoading(false);
            })
            .catch(err => {
                console.error('Error fetching tracks:', err);
                setError(err.message);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <div className="loading-spinner">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-message">
                <h3>Error Loading Tracks</h3>
                <p>{error}</p>
                <p>Please make sure you're logged in and try again.</p>
            </div>
        );
    }

    if (!tracks || !tracks.items || tracks.items.length === 0) {
        return <div className="alert alert-warning">No tracks available</div>;
    }

    // Render tracks
    return (
        <div className="profile-card">
            <h3 className="mb-4">Top Artists</h3>
            <div className="tracks-grid">
                {tracks.items.map(item => (
                    <div key={item.id} className="track-item">
                        {item.images && item.images.length > 0 ? (
                            <img 
                                src={item.images[0].url} 
                                alt={item.name} 
                                className="track-image"
                            />
                        ) : (
                            <div className="track-image bg-secondary d-flex justify-content-center align-items-center">
                                <span className="text-white">No Image</span>
                            </div>
                        )}
                        <div className="track-info">
                            <div className="track-name">{item.name}</div>
                            <div className="track-genres">
                                {item.genres && item.genres.length > 0 
                                    ? item.genres.slice(0, 2).join(', ') 
                                    : 'No genres'}
                            </div>
                            <div className="track-popularity">
                                Popularity: {item.popularity}/100
                            </div>
                            <a 
                                href={item.external_urls?.spotify} 
                                target="_blank" 
                                rel="noopener noreferrer"
                                className="btn btn-sm btn-outline-success mt-2"
                            >
                                Open in Spotify
                            </a>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

// UserProfile component to display user data
const UserProfile = () => {
    const [user, setUser] = React.useState(null);
    const [loading, setLoading] = React.useState(true);
    const [error, setError] = React.useState(null);

    React.useEffect(() => {
        // Fetch user profile data from the Spring Boot backend
        fetch('/api/users/profile')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch user profile');
                }
                return response.json();
            })
            .then(data => {
                setUser(data);
                setLoading(false);
            })
            .catch(err => {
                console.error('Error fetching user profile:', err);
                setError(err.message);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <div className="loading-spinner">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-message">
                <h3>Error</h3>
                <p>{error}</p>
                <p>Please make sure you're logged in and try again.</p>
            </div>
        );
    }

    if (!user) {
        return <div className="alert alert-warning">No user data available</div>;
    }

    // Render user profile
    return (
        <React.Fragment>
            <div className="profile-card">
                <div className="profile-header">
                    {user.images && user.images.length > 0 ? (
                        <img 
                            src={user.images[0].url} 
                            alt={user.display_name} 
                            className="profile-image"
                        />
                    ) : (
                        <div className="profile-image bg-secondary d-flex justify-content-center align-items-center">
                            <span className="text-white fs-1">
                                {user.display_name ? user.display_name.charAt(0).toUpperCase() : '?'}
                            </span>
                        </div>
                    )}

                    <div className="profile-info">
                        <h2 className="profile-name">{user.display_name}</h2>
                        <div className="profile-id">ID: {user.id}</div>
                        <a 
                            href={user.external_urls?.spotify} 
                            target="_blank" 
                            rel="noopener noreferrer"
                            className="btn btn-success btn-sm"
                        >
                            Open in Spotify
                        </a>
                    </div>
                </div>

                <div className="profile-stats">
                    <div className="stat-item">
                        <div className="stat-value">{user.followers?.total || 0}</div>
                        <div className="stat-label">Followers</div>
                    </div>
                    <div className="stat-item">
                        <div className="stat-value">{user.type || 'User'}</div>
                        <div className="stat-label">Account Type</div>
                    </div>
                </div>
            </div>

            <UserTracks />
        </React.Fragment>
    );
};

// Main App component
const App = () => {
    return (
        <div className="app-container">
            <UserProfile />
        </div>
    );
};

// Render the React application
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);
