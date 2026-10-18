import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import api from '../api/axios'

export default function Profile() {
  const { username } = useParams()
  const [profile, setProfile] = useState(null)

  useEffect(() => {
    api.get(`/users/${username}/profile`).then(({ data }) => setProfile(data))
  }, [username])

  if (!profile) return <div className="container"><p className="muted">Loading...</p></div>

  return (
    <div className="container" style={{ maxWidth: 500 }}>
      <div className="card">
        <h2>u/{profile.username}</h2>
        {profile.bio && <p>{profile.bio}</p>}
        <p className="muted">Joined {new Date(profile.createdAt).toLocaleDateString()}</p>
        <div style={{ display: 'flex', gap: 24, marginTop: 12 }}>
          <div><strong>{profile.karma}</strong><div className="muted">Karma</div></div>
          <div><strong>{profile.postCount}</strong><div className="muted">Posts</div></div>
          <div><strong>{profile.commentCount}</strong><div className="muted">Comments</div></div>
        </div>
      </div>
    </div>
  )
}
